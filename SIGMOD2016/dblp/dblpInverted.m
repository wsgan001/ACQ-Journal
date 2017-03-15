set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x = [1, 2, 3, 4, 5];
y1 = [5720.999618	2074.961622	1410.909602	1102.100302	411.7221082];
y2 = [4862.346676	1757.200302	933.710148	728.538995	230.5936899];
y3 = [84262.33	53823.22	31480.96	21765.56	19882.12];
y4 = [18935	16584.97	7475.86	5149.83	4621.99];


p1= semilogy(x, y1, '-ks');
hold on;
p2 = semilogy(x, y2, '-kd');
hold on;
p3 = semilogy(x, y3, '-kv');
hold on;
p4 = semilogy(x, y4, '-k^');
hold on;

xlabel('k');
ylabel('time (ms)');

axis([0.5 5.5 0.0 1000000]);

set(gca, 'xtick', 1:5, 'XTickLabel', {'4','5','6','7','8'});
%leg=legend('basic-g','basic-w','Inc-S','Inc-T','Dec', 1);
%set(leg,'edgecolor','white');

set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'FontSize', 12);
leg1 = legend([p1, p2],'Inc-S','Inc-T');
set(leg1,'edgecolor','white');

set(gca, 'LineWidth', 1.5);
ah=axes('position',get(gca,'position'),'visible','off');
set(gca, 'FontSize', 12);
leg2 = legend(ah,[p3, p4],'Inc-S*','Inc-T*');
set(leg2,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);


