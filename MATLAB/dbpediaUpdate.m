set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x=[1, 2, 3, 4, 5];
y1 = [9.5001272241E4	8.6677190087E4	8.4358334117E4	9.4522824314E4	8.5216540252E4];
y2 = [5.02068257	12.81094398	 91.03924596999999	333.59492743	500.79473253];
y3 = [1378.21145845	850.83470587	2028.16585109	2073.4171693799999	1107.43494956];

p1= semilogy(x, y1, '-k^');
hold on;
p2 = semilogy(x, y2, '-ko');
hold on;
p3 = semilogy(x, y3, '-kd');

xlabel('the core number of the vertex');
ylabel('time (ms)');

axis([0.5 5.5 0 1E5]);

set(gca, 'xtick', 1:5, 'XTickLabel', {'5','10','15','20','25'});
leg=legend('rebuild','insert','delete', 2);
set(leg,'edgecolor','white');
set(gca,'YTick',[10^1,10^2,10^3,10^4,10^5]);

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);