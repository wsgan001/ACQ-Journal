set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x=[1, 2, 3, 4, 5];
y2 = [1.846707003E2	9.608940028E1	3.757015287E1	1.466826665E1	3.16248186E0];
y1 = [5.358990280967E3	5.238372014669E3	5.234742994811E3	5.216843168482E3	5.2521347439E3];

p1= semilogy(x, y1, '-k^');
hold on;
p2 = semilogy(x, y2, '-ko');
hold on;

xlabel('the core number of the vertex');
ylabel('time (ms)');

axis([0.5 5.5 1.0 10000]);

set(gca, 'xtick', 1:5, 'XTickLabel', {'5','10','15','20','25'});
leg=legend('rebuild','update', 2);
set(leg,'edgecolor','white');
set(gca,'ytick',[10^0,10^1,10^2,10^3,10^4]);
set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);